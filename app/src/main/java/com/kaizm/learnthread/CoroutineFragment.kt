package com.kaizm.learnthread

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.kaizm.learnthread.databinding.FragmentCoroutineBinding
import kotlinx.coroutines.*
import java.lang.Runnable

class CoroutineFragment : Fragment() {
    private lateinit var binding: FragmentCoroutineBinding
    private lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoroutineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btn1.setOnClickListener {
            lifecycleScope.launch {
                var x = 0
                runnable = Runnable {
                    binding.tv1.text = "Run on ${Thread.currentThread().name}  = $x"
                    x++
                    Handler().postDelayed(runnable, 100)
                }
                runnable.run()
            }
        }

        var job: Job = Job()
        binding.btn2.setOnClickListener {
            lifecycleScope.launch {
                val startTime = System.currentTimeMillis()
                job = launch(Dispatchers.Default) {
                    var nextPrintTime = startTime
                    var i = 0
                    while (isActive) {
                        if (System.currentTimeMillis() >= nextPrintTime) {
                            println("job: I'm sleeping ${i++} ...")
                            nextPrintTime += 500L
                        }
                    }
                }
                delay(1300L)
                println("job: I'm tired of waiting!")

            }

            job.invokeOnCompletion {
                if (it == null) {
                    Log.e(TAG, "Parent Job finish")
                } else {
                    Log.e(TAG, "Parent Job fail $it")
                }
            }
        }

        binding.btn2Cancel.setOnClickListener {
            job.cancel()
            println("job: Now I can quit.")
        }


        binding.btn3.setOnClickListener {
            binding.tv3.text = "Running..."
            lifecycleScope.launch(Dispatchers.IO) {
                launch(Dispatchers.Main) {
                    for (i in 1..10) {
                        println("Job $i ${Thread.currentThread().name}")
                        yield()
                        delay(200)

                    }
                }

                for (i in 1..10) {
                    println("Main $i ${Thread.currentThread().name}")
                    yield()
                    delay(200)
                }
            }
        }

    }
}

private suspend fun downloadSomething(name: String, delay: Long) {
    Log.e(TAG, "Job $name Run on ${Thread.currentThread().name} Start")
    delay(delay)
    Log.e(TAG, "Job $name Run on ${Thread.currentThread().name} Done")
}
