package happy.mjstudio.animationsample.ui

import android.animation.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import happy.mjstudio.animationsample.R
import kotlinx.android.synthetic.main.fragment_animator.*

class AnimatorFragment : Fragment() {

    companion object {
        const val DEFAULT_DURATION = 300L
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_animator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //region ValueAnimator
        ValueAnimator.ofFloat(0f, 500f).apply {
            addUpdateListener { animation ->
                val value = animation?.animatedValue as? Float ?: throw NullPointerException()

                /* NullPointerException 발생 가능, 위험한 방법 */
                try {
                    imageView.x = value
                } catch (ex: Exception) {
                }
            }
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            duration = DEFAULT_DURATION
            start()
        }
        //endregion

        //region ObjectAnimator
        ObjectAnimator.ofFloat(imageView2, "x", 0f, 1000f).apply {

            interpolator = LinearInterpolator() // AccelerateDecelerateInterpolator 가 원래 Default임
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            duration = DEFAULT_DURATION
            start()
        }
        //endregion

        //region ViewPropertyAnimator

        /**
         * 초기 위치가 설정안돼있을 때 y를 움직이면 (0, 0)부터 시작해서 GlobalLayoutListener를 설정
         *
         * 뒤에 By 가 붙으면 현재 값으로부터 움직임 (current value = offset)
         */
        imageView3.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                imageView3.viewTreeObserver.removeOnGlobalLayoutListener(this)

                imageView3.animate().x(500f).apply {
                    duration = 3000L

                    this.setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {

                            this@apply.setListener(null) // 리스너를 해제해줘야 함

                            if (imageView3 != null)
                                imageView3.animate().xBy(-500f).apply {
                                    duration = 1500L
                                    start()
                                }
                        }
                    })

                    start()
                }
            }
        })

        //endregion

        //region AnimatorSet

        val animator1 = ObjectAnimator.ofFloat(imageView4, "x", 0f, 500f).apply {
            duration = 5000L
        }
        val animator2 = ObjectAnimator.ofFloat(imageView5, "x", 0f, 500f).apply {
            duration = 2000L
        }

        AnimatorSet().apply {
            this.playSequentially(animator1, animator2)

            start()
        }

        //endregion
    }
}
