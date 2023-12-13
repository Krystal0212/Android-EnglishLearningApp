plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

val lifecycleVersion = "2.2.0"
val retrofitVersion = "2.9.0"
val okhttpVersion = "4.9.0"
val glideVersion = "4.11.0"

android {
    namespace = "com.example.loginactivity"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.loginactivity"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.navigation:navigation-fragment:2.5.3")
    implementation("androidx.navigation:navigation-ui:2.5.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.firebaseui:firebase-ui-auth:7.2.0")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")

    // Circle image view
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Load image from URL
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("androidx.viewpager2:viewpager2:1.0.0")
    //noinspection GradleCompatible
    implementation("androidx.databinding:databinding-runtime:7.2.0")

    implementation("androidx.security:security-crypto:1.0.0")
    implementation("com.airbnb.android:lottie:3.4.0")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.17")
    implementation ("com.google.firebase:firebase-storage:20.2.1")
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("me.relex:circleindicator:2.1.6")
    implementation ("com.google.android.flexbox:flexbox:3.0.0")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("com.opencsv:opencsv:5.0")

}
