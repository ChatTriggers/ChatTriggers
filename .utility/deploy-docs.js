if (process.env.TRAVIS_REPO_SLUG === "ChatTriggers/ChatTriggers"
    && process.env.TRAVIS_PULL_REQUEST == "false"
    && (process.env.TRAVIS_BRANCH === "master" || process.env.TRAVIS_BRANCH === "travis")) {

    if (process.env.TRAVIS_COMMIT_MESSAGE.includes("-n") || process.env.TRAVIS_COMMIT_MESSAGE.includes("--no")) {
        console.log("Not deploying docs!");
        return;
    }

    console.log("Publishing userdocs...");

    var { exec } = require('child_process');

    exec("cp -R ../build/javadoc/ ./javadocs");

    var FtpClient = require('ftp-client');
    var client = new FtpClient({
        host: 'chattriggers.com',
        port: 21,
        user: process.env.FTP_USER,
        password: process.env.FTP_PASSWORD
    }, {
        logging: 'basic'
    });


    client.connect(function () {
        client.upload(['javadocs/**'], '/root/web/static/home/javadocs', {
            baseDir: 'javadocs',
            overwrite: 'all'
        }, function (result) {
            console.log(result);
            console.log("Published userdocs to production.");
        });
    });
}
