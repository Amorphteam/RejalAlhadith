package com.papyrus.mehdok.rejalalhadith

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.papyrus.mehdok.rejalalhadith.database.DataRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DataRepositoryImpl.getInstance().getRejals(5)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ rejals ->
                    rejals.forEach {
                        Log.w("MainActivity", "id: ${it.ID}")
                    }

                },
                        { e ->
                            e.printStackTrace()
                        })


    }
}
