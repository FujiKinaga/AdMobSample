package fujikinaga.sample.admobsample.ui.main

import fujikinaga.sample.admobsample.ad.Ad

interface OnFeedActionListener {
    fun getAd(): Ad?
    fun onCellClick()
}