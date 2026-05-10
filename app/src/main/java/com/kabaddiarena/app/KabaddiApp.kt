package com.kabaddiarena.app

import android.app.Application
import com.kabaddiarena.app.data.db.KabaddiDatabase

class KabaddiApp : Application() {

    val database: KabaddiDatabase by lazy {
        KabaddiDatabase.getDatabase(this)
    }
}