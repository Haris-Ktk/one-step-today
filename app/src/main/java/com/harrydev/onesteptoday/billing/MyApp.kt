package com.harrydev.onesteptoday.billing

import android.app.Application
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Purchases.configure(
            PurchasesConfiguration.Builder(
                this,
                "goog_eLqWiTOForiRhGwyzKXkGGSChJI"
            ).build()
        )
    }
}
