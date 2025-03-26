plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
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
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

//nav graph
    val nav_version = "2.5.3" // או הגרסה העדכנית ביותר
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")


    


        implementation("androidx.navigation:navigation-fragment-ktx:2.7.5") //תוספים של ה nav graph
        implementation("androidx.navigation:navigation-ui-ktx:2.7.5")



    // Design
    implementation ("com.google.android.material:material:1.8.0")
    implementation ("com.google.android.material:material:1.11.0")//bottom bar

    //picasso
    implementation("com.squareup.picasso:picasso:2.8")


    // Google Maps
    implementation ("com.google.android.gms:play-services-maps:18.0.2")

    // API data fetching
    implementation ("com.android.volley:volley:1.2.1")
    //Room
    implementation("androidx.room:room-runtime:2.6.1") // או הגרסה העדכנית ביותר
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.code.gson:gson:2.8.8")
    //firebase
    implementation ("com.google.firebase:firebase-auth-ktx:22.3.1")
    implementation ("com.google.firebase:firebase-bom:32.7.0")


}


