// Top-level build file
// This file defines plugins and versions shared across all modules

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // Firebase: Google Services (Analytics, Firestore, etc.)
    id("com.google.gms.google-services") version "4.4.4" apply false
    // Firebase Crashlytics (for crash reporting)
    id("com.google.firebase.crashlytics") version "3.0.2" apply false
    // Firebase Performance Monitoring
    id("com.google.firebase.firebase-perf") version "1.4.2" apply false
}
