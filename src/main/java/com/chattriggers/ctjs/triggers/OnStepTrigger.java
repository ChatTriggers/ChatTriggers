// package com.chattriggers.ctjs.triggers;
//
// import com.chattriggers.ctjs.minecraft.wrappers.Client;
//
// public class OnStepTrigger extends OnTrigger {
//     private Long fps = 60L;
//     private Long delay = null;
//     private Long systemTime;
//     private Long elapsed;
//
//     protected OnStepTrigger(Object method) {
//         super(method, TriggerType.STEP);
//         this.systemTime = Client.getSystemTime();
//         this.elapsed = 0L;
//     }
//
//     /**
//      * Sets the frames per second that the trigger activates.
//      * This is limited to 1 step per second.
//      * @param fps the frames per second to set
//      * @return the trigger for method chaining
//      */
//     public OnStepTrigger setFps(long fps) {
//         this.fps = fps < 1 ? 1L : fps;
//
//         this.systemTime = Client.getSystemTime() + (1000 / this.fps);
//
//         return this;
//     }
//
//     /**
//      * Sets the delay in seconds between the trigger activation.
//      * This is limited to one step every second. This will override {@link #setFps(long)}.
//      * @param delay The delay in seconds
//      * @return the trigger for method chaining
//      */
//     public OnStepTrigger setDelay(long delay) {
//         this.delay = delay < 1 ? 1L : delay;
//
//         this.systemTime = Client.getSystemTime() - this.delay * 1000;
//
//         return this;
//     }
//
//     @Override
//     public void trigger(Object... args) {
//         if (this.delay == null) {
//             // run trigger based on set fps value (60 per second by default)
//             while (this.systemTime < Client.getSystemTime() + (1000 / this.fps)) {
//                 this.elapsed++;
//                 callMethod(this.elapsed);
//                 this.systemTime += (1000 / this.fps);
//             }
//         } else {
//             // run trigger based on set delay in seconds
//             while (Client.getSystemTime() > this.systemTime + this.delay * 1000) {
//                 this.elapsed++;
//                 callMethod(this.elapsed);
//                 this.systemTime += this.delay * 1000;
//             }
//         }
//     }
// }
