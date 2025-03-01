plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.adoptadog"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.adoptadog"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    dependencies {
        implementation(platform("com.google.firebase:firebase-bom:31.0.0"))  // Firebase BOM
        implementation("com.google.firebase:firebase-auth")   // Firebase Authentication
        implementation("com.google.firebase:firebase-firestore")  // Firebase Firestore
        implementation("com.google.firebase:firebase-storage")  // Firebase Storage


        implementation("androidx.navigation:navigation-fragment-ktx:2.7.5") //תוספים של ה nav graph 
        implementation("androidx.navigation:navigation-ui-ktx:2.7.5")

    }


    // Design
    implementation ("com.google.android.material:material:1.8.0")

    // Google Maps
    implementation ("com.google.android.gms:play-services-maps:18.0.2")

    // API data fetching
    implementation ("com.android.volley:volley:1.2.1")
}


