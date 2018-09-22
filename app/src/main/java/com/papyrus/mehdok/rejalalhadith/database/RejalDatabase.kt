package com.papyrus.mehdok.rejalalhadith.database

import com.raizlabs.android.dbflow.annotation.Database

@Database(name = RejalDatabase.NAME, version = RejalDatabase.VERSION, backupEnabled = true, consistencyCheckEnabled = true)
class RejalDatabase {
    companion object {
        const val NAME = "REJAL"
        const val VERSION = 1
    }

}