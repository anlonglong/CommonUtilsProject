package com.allon.commonutilsproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.otaliastudios.cameraview.Facing
import husaynhakeem.io.facedetector.FaceDetector
import husaynhakeem.io.facedetector.models.Frame
import husaynhakeem.io.facedetector.models.Size
import kotlinx.android.synthetic.main.activity_face_detector.*

class FaceDetectorActivity : AppCompatActivity() {

    private val faceDetector: FaceDetector by lazy {
        FaceDetector(facesBoundsOverlay)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_detector)
        setupCamera()
    }

    private fun setupCamera() {
        FirebaseApp.initializeApp(this)
        cameraView.addFrameProcessor {
            faceDetector.process(Frame(
                    data = it.data,
                    rotation = it.rotation,
                    size = Size(it.size.width, it.size.height),
                    format = it.format,
                    isCameraFacingBack = cameraView.facing == Facing.FRONT))
        }

        revertCameraButton.setOnClickListener {
            cameraView.toggleFacing()
        }
    }

    override fun onResume() {
        super.onResume()
        cameraView.start()
    }

    override fun onPause() {
        super.onPause()
        cameraView.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraView.destroy()
    }

    companion object {
        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, FaceDetectorActivity::class.java))
        }
    }
}
