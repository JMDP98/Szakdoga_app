import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import com.example.fomenu.R
import com.example.fomenu.ui.home.HomeViewModel

class PopupDialog(context:
                  Context,
                  private val usernameListener: UsernameListener,
                  private val homeViewModel: HomeViewModel
) : Dialog(context) {

    interface UsernameListener {
        fun onUsernameEntered(username: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup)

        val enterButton = findViewById<Button>(R.id.close_button)
        enterButton.setOnClickListener {
            val username = findViewById<EditText>(R.id.UserPopUp).text.toString()
            usernameListener.onUsernameEntered(username)
            homeViewModel.setUsername(username)
            dismiss()
        }
    }
}
