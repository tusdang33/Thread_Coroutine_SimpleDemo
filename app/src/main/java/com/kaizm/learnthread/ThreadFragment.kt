package com.kaizm.learnthread

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kaizm.learnthread.databinding.FragmentThreadBinding

class ThreadFragment : Fragment() {
    private lateinit var binding: FragmentThreadBinding
    private lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentThreadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding.btn1.setOnClickListener {
            var x = 0
            runnable = Runnable {
                binding.tv1.text = "Run on ${Thread.currentThread().name}  = $x"
                x++
                Handler().postDelayed(runnable, 100)
                Thread.yield()
            }
            runnable.run()
        }

        var thread = Thread()
        binding.btn2.setOnClickListener {
            thread = Thread {
                val startTime = System.currentTimeMillis()
                var nextPrintTime = startTime
                var i = 0
                try {
                    while (!thread.isInterrupted) {
                        if (System.currentTimeMillis() >= nextPrintTime) {
                            println("Thread: I'm sleeping ${i++} ...")
                            nextPrintTime += 500L
                        }
                    }
                } catch (e: InterruptedException) {
                    Log.e(TAG, "on ${Thread.currentThread().name} Fail: ${e.localizedMessage}")
                }
            }
            thread.start()
            Thread.sleep(1300L)
            println("Thread: I'm tired of waiting!")
        }

        binding.btn2Cancel.setOnClickListener {
            thread.interrupt()
            println("Thread: Now I can quit.")
        }

        binding.btn3.setOnClickListener {
            binding.tv3.text = "Running..."
            Thread {
                try {
                    for (i in 1..10) {
                        Thread.sleep(200)
                        println("Thread 1 $i ${Thread.currentThread().name}")
                        Thread.yield()
                    }
                } catch (e: InterruptedException) {
                    Log.e(TAG, "Fail ${e.localizedMessage}")
                }
            }.start()

            Thread {
                for (i in 1..10) {
                    Thread.sleep(200)
                    println("Thread 2 $i ${Thread.currentThread().name}")
                    Thread.yield()
                }
            }.start()
        }
    }
}