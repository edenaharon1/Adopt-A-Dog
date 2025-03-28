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
    // AndroidX and Material
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("com.google.android.material:material:1.11.0")
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Navigation
    val nav_version = "2.7.5"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:31.0.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")

    


        implementation("androidx.navigation:navigation-fragment-ktx:2.7.5") //תוספים של ה nav graph
        implementation("androidx.navigation:navigation-ui-ktx:2.7.5")



    // Design
    implementation ("com.google.android.material:material:1.8.0")
    implementation ("com.google.android.material:material:1.11.0")//bottom bar
    implementation ("de.hdodenhof:circleimageview:3.1.0")


    //picasso
    implementation("com.squareup.picasso:picasso:2.8")


    // Google Maps
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // REST API (Volley)
    implementation("com.android.volley:volley:1.2.1")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // Other utils
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.google.android.libraries.places:places:3.3.0")

    //firebase
    implementation ("com.google.firebase:firebase-auth-ktx:22.3.1")
    implementation ("com.google.firebase:firebase-bom:32.7.0")
    //edit posts
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")


}


    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
