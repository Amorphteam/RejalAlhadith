package org.masaha.rejalalhadith.customviews

interface UiToggleListener {
    /**
     * Called when the recycler view scrolled upward
     * you can use it to show some ui element like fab
     */
    fun showUI()

    /**
     * Called when the recycler view scrolled downward
     * you can use it to hide some ui element like fab
     */
    fun hideUI()
}