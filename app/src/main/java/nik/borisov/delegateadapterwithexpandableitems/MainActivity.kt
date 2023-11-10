package nik.borisov.delegateadapterwithexpandableitems

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import nik.borisov.delegateadapterwithexpandableitems.databinding.ActivityMainBinding
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.core.CompositeAdapter
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.impl.BaseInfoAdapter
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.impl.DescriptionAdapter
import nik.borisov.delegateadapterwithexpandableitems.delegateadapter.impl.ProfileInfoAdapter

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<MainViewModel>()

    private val compositeAdapter by lazy {
        CompositeAdapter.Builder()
            .add(BaseInfoAdapter())
            .add(DescriptionAdapter())
            .add(ProfileInfoAdapter(
                onMessageClickListener = {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
            ))
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recyclerView.adapter = compositeAdapter

        viewModel.content.observe(this) {
            compositeAdapter.submitList(it)
        }
    }
}