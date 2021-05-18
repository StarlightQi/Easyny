package easyny.ludashen.com.easyny.view;
import android.content.Context;
import android.view.LayoutInflater;

import easyny.ludashen.com.easyny.DragView;
import easyny.ludashen.com.easyny.R;


public class FloatBallView extends DragView {
	public FloatBallView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.float_small_drag_ball, this);
	}
}
