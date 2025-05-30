plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.weatherapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.weatherapp"
        minSdk = 24
        targetSdk = 35
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
        compose = true
    }
    viewBinding{
        enable=true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.core.ktx)
    implementation(libs.androidx.junit.ktx)
    //Testing
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation ("org.hamcrest:hamcrest:2.2")
    androidTestImplementation ("org.hamcrest:hamcrest-library:2.2")

    androidTestImplementation ("androidx.arch.core:core-testing:2.1.0")
    testImplementation ("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation ("androidx.test:core-ktx:1.5.0")
    testImplementation ("org.robolectric:robolectric:4.11")
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    testImplementation ("org.hamcrest:hamcrest:2.2")
    testImplementation ("org.hamcrest:hamcrest-library:2.2")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("org.robolectric:robolectric:4.12.2")
    testImplementation(libs.junit)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
    testImplementation ("io.mockk:mockk-android:1.13.17")
    testImplementation ("io.mockk:mockk-agent:1.13.17")
            testImplementation ("androidx.arch.core:core-testing:2.1.0")
    testImplementation ("androidx.test:core-ktx:1.5.0")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3 ")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    // testImplementation ("junit:junit:4.13.2")


    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //retrofit
    implementation("com.google.code.gson:gson:2.9.0")
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.github.bumptech.glide:compose:1.0.0-beta01")
    //room
    val room_version = "2.6.1"
    implementation ("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation ("androidx.room:room-ktx:$room_version")

    //ViewModel & livedata
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    //Coroutine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

    //implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")

    //lotti
    implementation ("com.airbnb.android:lottie-compose:6.1.0")
    // splash
    implementation ("androidx.core:core-splashscreen:1.0.0")
    //nav
    val nav_version = "2.8.8"
    implementation("androidx.navigation:navigation-compose:$nav_version")
    //sharedPref
    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    //location
    //implementation("com.google.android.gms:play-services-location:21.1.0")
    //map
   // implementation("com.google.maps.android:maps-compose:6.4.1")
    implementation("com.jakewharton.timber:timber:5.0.1")
    // Google Maps SDK for Android
    implementation(libs.places)
    implementation(libs.play.services.maps)

  // Google maps Compose
    implementation(libs.maps.compose)
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.30.1")

    implementation( "com.google.accompanist:accompanist-flowlayout:0.28.0")




}