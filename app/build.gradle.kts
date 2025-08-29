plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.tatanstudios.astropollocliente"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.tatanstudios.astropollocliente"
        minSdk = 27
        targetSdk = 36
        versionCode = 9
        versionName = "2.0.0"

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


    // OneSignal
    implementation("com.onesignal:OneSignal:[5.0.0, 5.99.99]")

    // Navigation and Layout
    implementation("androidx.navigation:navigation-compose:2.8.9")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.1")

    // Compose UI Components
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("androidx.compose.ui:ui:1.7.8")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.8")
    implementation("androidx.compose.material:material:1.7.8")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")
    implementation("io.reactivex.rxjava3:rxjava:3.1.5")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")


    // Lifecycle and Data
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.8")
    implementation("com.github.GrenderG:Toasty:1.5.2")
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Material and Foundation
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.foundation:foundation:1.6.7")

    // Navigation and UI Controllers
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")
    implementation("com.google.accompanist:accompanist-pager:0.32.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.32.0")

    // Google Services
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    implementation("com.google.android.gms:play-services-maps:19.1.0")
    implementation("com.google.android.gms:play-services-location:21.1.0")

    // Image Loading
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Permissions and Utils
    implementation("com.google.accompanist:accompanist-permissions:0.36.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("id.zelory:compressor:3.0.1")
    implementation("com.beust:klaxon:5.5")

    // Maps
    implementation("com.google.maps.android:maps-compose:4.3.3")
    implementation("com.github.prabhat1707:EasyWayLocation:2.4")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore")

    implementation("com.github.imperiumlabs:GeoFirestore-Android:v1.5.0")
    implementation("com.google.maps.android:android-maps-utils:2.3.0")


    implementation("com.google.android.gms:play-services-auth:21.2.0")

    implementation("com.airbnb.android:lottie-compose:6.3.0")


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}