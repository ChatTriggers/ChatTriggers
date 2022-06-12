package com.chattriggers.ctjs.minecraft.listeners.events

import net.minecraft.client.audio.ISound

//#if MC>=11701
//$$ import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
//#endif

class SoundEvent(val sound: ISound) : CancellableEvent()

class NoteBlockEvent(eventID: Int) : CancellableEvent() {
    val note: Note
    val octave: Octave
    //#if MC<=11202
    lateinit var instrument: Instrument
    //#elseif MC>=11701
    //$$ lateinit var instrument: NoteBlockInstrument
    //#endif
        internal set

    init {
        note = Note.values()[eventID % 12]
        octave = Octave.values()[eventID / 12]
    }

    //#if MC<=11202
    internal fun setInstrument(instrument: Int) {
        val instruments = Instrument.values()
        this.instrument = instruments[instrument.coerceIn(0, instruments.size - 1)]
    }
    //#endif

    enum class Note {
        F_SHARP,
        G,
        G_SHARP,
        A,
        A_SHARP,
        B,
        C,
        C_SHARP,
        D,
        D_SHARP,
        E,
        F
    }

    //#if MC<=11202
    enum class Instrument {
        PIANO,
        BASSDRUM,
        SNARE,
        CLICKS,
        BASSGUITAR
    }
    //#endif

    enum class Octave {
        LOW,
        MIDDLE,
        HIGH
    }
}