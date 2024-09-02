package hr.algebra.pawprotectormobile.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val sharedString: MutableLiveData<String> = MutableLiveData()
}
