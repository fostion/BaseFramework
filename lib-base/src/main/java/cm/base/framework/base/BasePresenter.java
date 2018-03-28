package cm.base.framework.base;

import cm.base.framework.utils.RxLifeEvent;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

/**
 * p转接器
 */
public abstract class BasePresenter {

    private BehaviorSubject<RxLifeEvent> subject = BehaviorSubject.create();

    public void onStop(){
        subject.onNext(RxLifeEvent.STOP);
    }

    public void onDestory(){
        subject.onNext(RxLifeEvent.DESTROY);
    }

    public void onDeatch(){
        subject.onNext(RxLifeEvent.DETACH);
    }

    public Scheduler onUI(){
        return AndroidSchedulers.mainThread();
    }

    //绑定生命周期
    public <T> Observable.Transformer<T, T> bindLife() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.takeUntil(subject.skipWhile(new Func1<RxLifeEvent, Boolean>() {
                    @Override
                    public Boolean call(RxLifeEvent event) {
                        return event != RxLifeEvent.STOP && event != RxLifeEvent.DESTROY && event != RxLifeEvent.DETACH;
                    }
                }));
            }
        };
    }

}
