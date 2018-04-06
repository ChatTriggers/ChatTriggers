package com.chattriggers.ctjs.utils;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Prime {
    @Setter
    private Boolean status = false;
    @Getter
    @Setter
    private List<PrimeModule> purchasedModules;
    private String error;

    public boolean getStatus() {
        return status;
    }

    private void updatePrime() {
        if (this.error != null) {
            ChatLib.chat("&cERROR: " + error);
        }
        CTJS.getInstance().setPrime(this);
    }

    private class PrimeModule {
        String name;
        String version;
        boolean purchased;

        private PrimeModule(String name, String version, boolean purchased) {
            this.name = name;
            this.version = version;
            this.purchased = purchased;
        }
    }

    public class PrimeConfirmation {
        String date;
        PrimeModule purchasedModule;
        Boolean purchasedSubscription;

        public void checkConfirmation() {
            if (this.purchasedModule != null) {
                purchasedModules.add(this.purchasedModule);
            }

            if (this.purchasedSubscription != null) {
                status = this.purchasedSubscription;
            }

            updatePrime();
        }
    }
}
