package com.scwang.wave.app.fragment


import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.larswerkman.lobsterpicker.OnColorListener
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider
import com.scwang.wave.MultiWaveHeader
import com.scwang.wave.ShapeType
import com.scwang.wave.app.R
import com.scwang.wave.app.util.StatusBarUtil
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar

/**
 * A simple [Fragment] subclass.
 */
class WaveConsoleFragment : Fragment(), DiscreteSeekBar.OnProgressChangeListener {
    private val toolbar by lazy { view?.findViewById<Toolbar>(R.id.toolbar) }
    private val sliderCloseColor by lazy { view?.findViewById<LobsterShadeSlider>(R.id.sliderCloseColor) }
    private val sliderStartColor by lazy { view?.findViewById<LobsterShadeSlider>(R.id.sliderStartColor) }
    private val seekProgress by lazy { view?.findViewById<DiscreteSeekBar>(R.id.seekProgress) }
    private val seekVelocity by lazy { view?.findViewById<DiscreteSeekBar>(R.id.seekVelocity) }
    private val seekAlpha by lazy { view?.findViewById<DiscreteSeekBar>(R.id.seekAlpha) }
    private val seekAngle by lazy { view?.findViewById<DiscreteSeekBar>(R.id.seekAngle) }
    private val seekWave by lazy { view?.findViewById<DiscreteSeekBar>(R.id.seekWave) }
    private val seekNumber by lazy { view?.findViewById<DiscreteSeekBar>(R.id.seekNumber) }
    private val radioOval by lazy { view?.findViewById<RadioButton>(R.id.radioOval) }
    private val radioRoundRect by lazy { view?.findViewById<RadioButton>(R.id.radioRoundRect) }
    private val radioRect by lazy { view?.findViewById<RadioButton>(R.id.radioRect) }
    private val radioGroup by lazy { view?.findViewById<RadioGroup>(R.id.radioGroup) }
    private val checkBoxRunning by lazy { view?.findViewById<CheckBox>(R.id.checkBoxRunning) }
    private val checkBoxDirection by lazy { view?.findViewById<CheckBox>(R.id.checkBoxDirection) }
    private val multiWaveHeader by lazy { view?.findViewById<MultiWaveHeader>(R.id.multiWaveHeader) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wave_console, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)


        StatusBarUtil.immersive(activity)
        StatusBarUtil.setPaddingSmart(context, toolbar)
        toolbar?.setNavigationOnClickListener {
            activity?.finish()
        }

        seekAngle?.progress = multiWaveHeader?.gradientAngle ?: 0
        seekVelocity?.progress = ((multiWaveHeader?.velocity ?: 1f) * 10).toInt()
        seekAlpha?.progress = ((multiWaveHeader?.colorAlpha ?: 1f) * 100).toInt()
        seekProgress?.progress = ((multiWaveHeader?.progress ?: 1f) * 100).toInt()
        seekWave?.progress = ((multiWaveHeader?.waveHeight
            ?: 0) / Resources.getSystem().displayMetrics.density).toInt()
        checkBoxRunning?.isChecked = multiWaveHeader?.isRunning ?: false
        checkBoxDirection?.isChecked = multiWaveHeader?.scaleY == -1f

        seekWave?.setOnProgressChangeListener(this)
        seekAngle?.setOnProgressChangeListener(this)
        seekVelocity?.setOnProgressChangeListener(this)
        seekProgress?.setOnProgressChangeListener(this)
        seekAlpha?.setOnProgressChangeListener(this)
        seekNumber?.setOnProgressChangeListener(this)
        checkBoxRunning?.setOnCheckedChangeListener { _, value ->
            if (value) {
                multiWaveHeader?.start()
            } else {
                multiWaveHeader?.stop()
            }
        }
        checkBoxDirection?.setOnCheckedChangeListener { _, value ->
            multiWaveHeader?.scaleY = if (value) -1f else 1f
            context?.also {
                toolbar?.setBackgroundColor(
                    if (value) ContextCompat.getColor(
                        it,
                        R.color.colorPrimary
                    ) else 0
                )
            }
        }

        sliderStartColor?.addOnColorListener(object : OnColorListener {
            override fun onColorChanged(color: Int) {
                onColorSelected(color)
            }

            override fun onColorSelected(color: Int) {
                multiWaveHeader?.startColor = color
            }
        })
        sliderCloseColor?.addOnColorListener(object : OnColorListener {
            override fun onColorChanged(color: Int) {
                onColorSelected(color)
            }

            override fun onColorSelected(color: Int) {
                multiWaveHeader?.closeColor = color
            }
        })

        radioGroup?.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioRect -> multiWaveHeader?.shape = ShapeType.Rect
                R.id.radioOval -> multiWaveHeader?.shape = ShapeType.Oval
                R.id.radioRoundRect -> multiWaveHeader?.shape = ShapeType.RoundRect
            }
        }

    }

    override fun onProgressChanged(seekBar: DiscreteSeekBar, value: Int, fromUser: Boolean) {
        when (seekBar) {
            seekProgress -> multiWaveHeader?.progress = 1f * value / 100
            seekVelocity -> multiWaveHeader?.velocity = 1f * value / 10
            seekAlpha -> multiWaveHeader?.colorAlpha = 1f * value / 100
            seekAngle -> multiWaveHeader?.gradientAngle = value
        }
    }

    override fun onStartTrackingTouch(seekBar: DiscreteSeekBar) {
    }

    override fun onStopTrackingTouch(seekBar: DiscreteSeekBar) {
        if (seekWave == seekBar) {
            multiWaveHeader?.waveHeight = seekBar.progress
        } else if (seekNumber == seekBar) {
            if (seekBar.progress == 2) {
                /**
                 * 格式-format
                 * offsetX offsetY scaleX scaleY velocity（dp/s）
                 * 水平偏移量 竖直偏移量 水平拉伸比例 竖直拉伸比例 速度
                 */
                multiWaveHeader?.setWaves("0,0,1,1,25\n90,0,1,1,25")
            } else {
                val waves =
                    "70,25,1.4,1.4,-26\n100,5,1.4,1.2,15\n420,0,1.15,1,-10\n520,10,1.7,1.5,20\n220,0,1,1,-15".split(
                        "\n"
                    )
                multiWaveHeader?.setWaves(waves.subList(0, seekBar.progress).joinToString("\n"))
            }
        }
    }
}
