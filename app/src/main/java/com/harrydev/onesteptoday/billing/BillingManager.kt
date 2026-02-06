package com.harrydev.onesteptoday.billing

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.harrydev.onesteptoday.utils.PrefManager
import com.revenuecat.purchases.*
import kotlinx.coroutines.launch

class BillingManager(
    private val context: Context,
    private val onEntitlementUpdated: (Boolean) -> Unit
) {
    companion object {
        const val ENTITLEMENT_PRO = "pro_lifetime_v2"
    }
    private val pref = PrefManager(context)
    private val scope = ProcessLifecycleOwner.get().lifecycleScope

    fun startConnection() {
        checkProStatus()
    }
    /* ---------------- PURCHASE ---------------- */
    fun launchPurchase(activity: Activity) {
        scope.launch {
            try {
                // 1. Fetch Offerings
                val offerings = Purchases.sharedInstance.awaitOfferings()

                val offering = offerings.current ?: return@launch
                val lifetimePackage = offering.lifetime ?: return@launch

                // 2. Build Params
                val params = PurchaseParams.Builder(activity, lifetimePackage).build()

                // 3. Execute Purchase
                val purchaseResult = Purchases.sharedInstance.awaitPurchase(params)

                // 4. Update Status
                updateProStatus(purchaseResult.customerInfo)
            } catch (e: PurchasesException) {
                onEntitlementUpdated(false)
            }
        }
    }
    /* ---------------- RESTORE ---------------- */
    fun restorePurchases() {
        scope.launch {
            try {
                val customerInfo = Purchases.sharedInstance.awaitRestore()
                updateProStatus(customerInfo)
            } catch (e: PurchasesException) {
            }
        }
    }
    /* ---------------- STATUS ---------------- */
    private fun checkProStatus() {
        scope.launch {
            try {
                val customerInfo = Purchases.sharedInstance.awaitCustomerInfo()
                updateProStatus(customerInfo)
            } catch (e: PurchasesException) {
            }
        }
    }
    /* ---------------- COMMON ---------------- */
    private fun updateProStatus(info: CustomerInfo?) {
        val isPro = info
            ?.entitlements
            ?.active
            ?.containsKey(ENTITLEMENT_PRO) == true

        pref.setProUser(isPro)
        onEntitlementUpdated(isPro)
    }
}
