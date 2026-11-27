plugins {
    id("com.android.application")
}

android {
    namespace = "app.dayflow.journal"
    compileSdk = 34

    defaultConfig {
        applicationId = "app.dayflow.journal"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "1.1"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
