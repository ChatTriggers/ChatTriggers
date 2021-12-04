package com.chattriggers.ctjs.engine.langs.js

import org.graalvm.polyglot.io.FileSystem
import java.io.IOException
import java.net.URI
import java.nio.channels.SeekableByteChannel
import java.nio.charset.Charset
import java.nio.file.*
import java.nio.file.attribute.FileAttribute
import java.nio.file.spi.FileSystemProvider
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

// Mostly taken from com.oracle.truffle.polyglot.FileSystems.NIOFileSystem
object JSFileSystem : FileSystem {
    private val TMP_FILE = System.getProperty("java.io.tmpdir")
    private val fs = FileSystems.getDefault()
    private var cwd: Path? = null
    private val fsProvider = FileSystemProvider.installedProviders().find {
        it.scheme == "file"
    }!!
    private val defaultFileNames = listOf("index.js", "index.mjs")

    private fun resolveDirectoryIndexImports(path: Path): Path {
        if (!path.exists() || !path.isDirectory())
            return path

        for (defaultFileName in defaultFileNames) {
            val newPath = path.resolve(defaultFileName)
            if (newPath.exists())
                return newPath
        }

        return path
    }

    override fun parsePath(uri: URI): Path = resolveDirectoryIndexImports(fsProvider.getPath(uri))

    override fun parsePath(path: String): Path = resolveDirectoryIndexImports(Paths.get(path))

    override fun checkAccess(path: Path, modes: MutableSet<out AccessMode>, vararg linkOptions: LinkOption) {
        if (LinkOption.NOFOLLOW_LINKS !in linkOptions) {
            fsProvider.checkAccess(resolveRelative(path), *modes.toTypedArray())
        } else if (modes.isEmpty()) {
            fsProvider.readAttributes(path, "isRegularFile", LinkOption.NOFOLLOW_LINKS)
        } else {
            throw UnsupportedOperationException("CheckAccess for NIO Provider is unsupported with non empty AccessMode and NOFOLLOW_LINKS.")
        }
    }

    override fun createDirectory(dir: Path, vararg attrs: FileAttribute<*>) {
        fsProvider.createDirectory(resolveRelative(dir), *attrs)
    }

    override fun delete(path: Path) {
        fsProvider.delete(resolveRelative(path))
    }

    override fun copy(source: Path, target: Path, vararg options: CopyOption) {
        fsProvider.copy(resolveRelative(source), resolveRelative(target), *options)
    }

    override fun move(source: Path, target: Path, vararg options: CopyOption) {
        fsProvider.move(resolveRelative(source), resolveRelative(target), *options)
    }

    override fun newByteChannel(
        path: Path,
        options: MutableSet<out OpenOption>,
        vararg attrs: FileAttribute<*>
    ): SeekableByteChannel {
        val resolved = resolveRelative(path)
        return try {
            fsProvider.newFileChannel(resolved, options, *attrs)
        } catch (e: UnsupportedOperationException) {
            fsProvider.newByteChannel(resolved, options, *attrs)
        }
    }

    override fun newDirectoryStream(dir: Path, filter: DirectoryStream.Filter<in Path>): DirectoryStream<Path> {
        val (resolvedPath, relativize) = if (!dir.isAbsolute && cwd != null) {
            cwd!!.resolve(dir) to true
        } else dir to false

        val result = fsProvider.newDirectoryStream(resolvedPath, filter)
        if (relativize)
            return RelativizeDirectoryStream(cwd!!, result)
        return result
    }

    override fun createLink(link: Path, existing: Path) {
        fsProvider.createLink(resolveRelative(link), resolveRelative(existing))
    }

    override fun createSymbolicLink(link: Path, target: Path, vararg attrs: FileAttribute<*>) {
        fsProvider.createSymbolicLink(resolveRelative(link), resolveRelative(target), *attrs)
    }

    override fun readSymbolicLink(link: Path): Path {
        return fsProvider.readSymbolicLink(link)
    }

    override fun readAttributes(path: Path, attributes: String, vararg options: LinkOption): MutableMap<String, Any> {
        return fsProvider.readAttributes(resolveRelative(path), attributes, *options)
    }

    override fun setAttribute(path: Path, attribute: String, value: Any, vararg options: LinkOption) {
        fsProvider.setAttribute(resolveRelative(path), attribute, value, *options)
    }

    override fun toAbsolutePath(path: Path): Path {
        if (path.isAbsolute)
            return path
        return cwd?.resolve(path) ?: path.toAbsolutePath()
    }

    override fun setCurrentWorkingDirectory(currentWorkingDirectory: Path) {
        if (!currentWorkingDirectory.isAbsolute)
            throw IllegalArgumentException("Current working directory must be absolute")

        val isDirectory = try {
            fsProvider.readAttributes(currentWorkingDirectory, "isDirectory")["isDirectory"] == true
        } catch (e: IOException) {
            false
        }

        if (!isDirectory)
            throw IllegalArgumentException("Current working directory must be a directory")

        cwd = currentWorkingDirectory
    }

    override fun toRealPath(path: Path, vararg linkOptions: LinkOption): Path {
        return resolveRelative(path).toRealPath(*linkOptions)
    }

    override fun getTempDirectory(): Path {
        if (TMP_FILE == null)
            throw IllegalStateException("java.io.tmpdir is not set")
        return parsePath(TMP_FILE)
    }

    override fun isSameFile(path1: Path, path2: Path, vararg options: LinkOption): Boolean {
        if (LinkOption.NOFOLLOW_LINKS !in options)
            return fsProvider.isSameFile(resolveRelative(path1), resolveRelative(path2))
        return super.isSameFile(path1, path2, *options)
    }

    private fun resolveRelative(path: Path) = resolveDirectoryIndexImports(if (!path.isAbsolute && cwd != null) {
        toAbsolutePath(path)
    } else path)

    private class RelativizeDirectoryStream constructor(
        private val folder: Path,
        private val delegateDirectoryStream: DirectoryStream<out Path>
    ) : DirectoryStream<Path> {
        override fun iterator(): MutableIterator<Path> {
            return RelativizeIterator(folder, delegateDirectoryStream.iterator())
        }

        @Throws(IOException::class)
        override fun close() {
            delegateDirectoryStream.close()
        }

        private class RelativizeIterator constructor(
            private val folder: Path,
            private val delegateIterator: Iterator<Path>
        ) : MutableIterator<Path> {
            override fun hasNext(): Boolean {
                return delegateIterator.hasNext()
            }

            override fun next(): Path {
                return folder.relativize(delegateIterator.next())
            }

            override fun remove() {
                throw UnsupportedOperationException()
            }
        }
    }
}
