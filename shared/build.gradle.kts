plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
}

kotlin {
    androidTarget {
        // No specific configuration needed here
    }

    jvmToolchain(11)

    sourceSets {
        commonMain.dependencies {
            // Common dependencies
        }
        androidMain.dependencies {
            // Android-specific dependencies
        }
    }
}

android {
    namespace = "com.bringe.calckmp"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
