# Android Dagger 활용하기

Dagger 의 기본 개념을 이해하고 있다는 것을 전재로 Android에서 Dagger 을 사용하는 방법을 공유하겠습니다. 만약 Dagger 개념에 대해서 이해 못하신 분은 [Dagger 간단히 알아보기 포스트](https://github.com/FaithDeveloper/TIL/blob/master/Android/Dagger%20%EA%B0%84%EB%8B%A8%ED%9E%88%20%EC%95%8C%EC%95%84%EB%B3%B4%EA%B8%B0.md)를 참고해주세요.

<br/>

Android Studio에서 Dagger 사용 시 Dependency 을 설정해야 합니다.

```
//dagger2
implementation 'com.google.dagger:dagger:2.16'
annotationProcessor 'com.google.dagger:dagger-compiler:2.16'
implementation 'com.google.dagger:dagger-android:2.16'
implementation 'com.google.dagger:dagger-android-support:2.16'
annotationProcessor 'com.google.dagger:dagger-android-processor:2.16'
```

- **com.google.dagger:dagger-android**
  dagger 에서 android Library 을 활용할 수 있는 라이브러리 입니다.
- **com.google.dagger:dagger-android-support**
  dagger 에서 android support Library 을 활용할 수 있는 라이브러리 입니다.

<br/>

안드로이드 프레임워크에서  Application 은 Singleton 으로서의 역할을 하며 컴포넌트들은 각각 고유의 라이프사이클을 기반으로 동작합니다. Application는 inject 를 수행하는 시작점이 됩니다. 따라서 Dagger 는 Application 단위에서 @Component 를 컴포넌트 단위에서 @Subcomponent 를 구성하고 inject 를 하는 것을 가이드로 주고 있습니다. 이것을 간단하게 하기 위해서 dagger.android 클래스들을 제공하고 있습니다. 

> **안드로이드 컴포넌트**
>
> - Application
> - Activity 
> - Service
> - Content Provider
> - Broadcast

<br/>

# MainActivity Presenter DI 구현 방법

MVP 패턴 (Model-View-Presenter) 으로 구성한 MainActivity의 Presenter를 DI 구현하는 방법을 공유하겠습니다. 

## MainAcitivy

```java
public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

	MainActivityContract.Presenter presenter;
	
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
```

Application는 `component` , MainActivity 는 `subcomponent` 을 구성하게 됩니다. MainActivity에서는  `inject` 역할을 담당해줄 `@Subcompoent` 와 외존성을 설정하는 `@Module` 을 구성합니다.

<br/>

**MainModule.class**

```java
@Module(subcomponents = MainFragmentComponent.class)
abstract class MainModule {
    @ActivityScope
    @Binds
    abstract MainActivityContract.View bindView(MainActivity activity);


    @ActivityScope
    @Binds
    public abstract MainActivityContract.Presenter bindPresenter(MainPresenterImpl mainPresenter);
}
```

<br/>

**ActivityScope.class**

```java
@Scope
public @interface ActivityScope {
}
```

<br/>

**MainComponent.class**

```java
@ActivityScope
@Subcomponent(modules = MainModule.class)
public interface MainComponent extends AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity>{
    }
}
```

`@Subcomponent` `annotation` 을 붙여주고 의존성 설정을 위해 MainModule 을 설정합니다.
**MainComponent.class** 에서 주의 깊게 살펴볼 것은 `AndroidInject` 를 상속하는 것입니다. `AndroidInject` 을 상속받으면서 interface로 **MainComponent** 을 구성하고 `@Subcomponent.Builder` 작성해주면 **@Subcomponent 작성**이 끝납니다.

> **AndroidInject**
>
> `AndroidInject` 는 `dagger.android` 에 포함하고 있는 클래스로 `@Subcomponent 의 코드`와 `inject 절차`를 간소화 합니다.  안드로이드에서는 프래임워크 LifeCycle 을 보면 앱 실행 시 Application 부터 시작하게 됩니다. 그렇기에 Application에서 @Component 를 구성하게 됩니다. @Subcomponent 인 Activity 에서 inject를 할 경우 **Component 호출 -> SubComponent 빌드 -> inject** 하는 코드들을 작성해야 합니다.  이것을 **AndroidInjector.inject** 한줄로 간소화 시켜줍니다.

- **AndroidInjector.inject  구성 안한 SubComponent 사용 예시**

```
public class FrombulationActivity extends Activity {
  @Inject Frombulator frombulator;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // DO THIS FIRST. Otherwise frombulator might be null!
    ((SomeApplicationBaseType) getContext().getApplicationContext())
        .getApplicationComponent()
        .newActivityComponentBuilder()
        .activity(this)
        .build()
        .inject(this);
    // ... now you can write the exciting code
  }
}
```

- **AndroidInjector.inject 구성한 SubComponent 사용 예시**

```
public class YourActivity extends Activity {
  public void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
  }
}
```

<br/>

## Application

`Application` 에서 `Component` 와 `Module` 을 구성하게 됩니다. 이유는 Application의 Lifecycle 에 따라 Component 는 `Singleton` 으로 관리 되어야하며 inject 또한 Application에서 이뤄지기 때문입니다. Module 안에 구성 또한 Application Lifecycle 을 적용하게 되어 `Singleton` 으로 적용 됩니다.

**AppComponent.class**

```java
@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class
})
public interface AppComponent {
    void inject(DaggerSampleApp daggerSampleApp);
}
```

`Sigleton` 을 `Scope` 로 구성하였으며 `Application` 에서 `inject` 발생하므로 **members-injection** 메서드 구성 하였습니다. 또한 `AndroidInjectionModule.class` 을 `Modules` 로 설정하면서 `dagger.android` 에서 제공하는 Class을 사용할 수 있습니다.

<br/>

**AppModule.class**

```java
@Module(subcomponents = MainComponent.class)
abstract class AppModule {
    @Singleton
    @Binds
    abstract DataSource bindDataSource(DataSourceImpl dataSource);

    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
    bindMainActivity(MainComponent.Builder builder);
}
```

`Android` 에서 `AndroidInjector` 사용을 위해서는 `AndroidInject.Factory` `Binds` 을 추가로 만들어줘야 합니다. 또한 `Activity` 는 해당하는 `@Subcomponent` 를 통해 `inject` 되므로 `Subcomponent.builder` 를 파라미터로 받는 바인터를 만들어 줍니다. `AndroidInjector.Factory` 는 `MultiBinds` 동작이 되므로  @ActivityKey(dagger.android) 를 지원하고 있습니다. ( 다른 컴포넌트들에 대한 key annotation 도 지원합니다. 예시. FragmentKey)

<br/>

## Application, MainActivity Inject

**DaggerSampleApp.class**

```java
public class DaggerSampleApp extends Application implements HasActivityInjector{
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        // inject
        DaggerAppComponent.create().inject(this);

    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
```

`Application` 에서 `HasActivityInject` 를 implement 해주면 `AndroidInject<Activity>` 를 리턴해주는 메서드를 작성할 수 있습니다. 리턴(return) 객체인 **AndroidInject<Activity**> 는 `AndroidInjectModule` 을 통해 바인딩(Binding) 할 수 있으며 이미 AppComponent 에서 modules 에 설정하여 install 을 하였습니다. 

따라서 `DispatchingAndroidInjector<Activity>` 멤버변수를 만들고 `@Inject` 를 달아주면 inject 시에 Application Module 로 설정한 Activity 을 바인딩을 해줍니다.  

`onCreate` 에서 AppComponent 생성 후 inject 를 실행시켜주는 코드를 작성하면 Application 설정은 마무리가 됩니다.

<br/>

**MainActivity.class**

```java
public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

    @Inject MainActivityContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidInjection.inject(this);
    }
}
```

`MainActivity` 에서는` AndroidInjection.inject(this);` 로 @Inject 멤버변수에 `inject` 를 수행합니다.

이러한 한줄 코드로 바인딩(Binding) 해주는 것 때문에 `dagger`을 사용하지 않았나 예상이 되는데요. 
`AndroidInject` 로 DI 구성한 Activity을 바인딩(Binding) 할 경우 기본적으로 바인더(Binds)를 생성하게됩니다. 따라서 별도로 `@Binds` 나 `@Provider` 설정하지 않아도 Activity 를 바인딩(Binding) 할 수 있습니다. 

<br/>

## PresenterImpl 구성

객체를 제공하려면 `@Provides` 또는 `@Inject constructor(생성자)` 로 제공하고 있습니다. `MainPresenterImpl` 을 `MainActivity` 에 제공 하기 위해서  `@Inject constructor(생성자)` 을 사용하면 다음과 같습니다.

```java
public class MainPresenterImpl implements MainActivityContract.Presenter {
    MainActivityContract.View view;
    DataSource dataSource;

    @Inject
    public MainPresenterImpl(MainActivityContract.View view, DataSource dataSource){
        this.view =view;
        this.dataSource = dataSource;
    }
}
```

<br/>

# MainFragment DI 구현 방법

`Fragment` 는 `Activity` 안에서만 동작하므로 부모가 Activity인 `@Subcomponent` 을 구성하게 됩니다. `Fragment` 도 Lifecycle 을 갖고 있기에 `@FragmentScope` 을 설정하였습니다.

<br/>

**FragmentScope.class**

```java
@Scope
public @interface FragmentScope {
}
```

<br/>

**MainFragmentComponent.class**

```java
@FragmentScope
@Subcomponent(modules = MainFragmentModule.class)
public interface MainFragmentComponent extends AndroidInjector<MainFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainFragment> {
    }
}
```

`Fragment` 에서 `AndroidInjector` 로 `inject` 를 수행하려면 부모가 되는`Activity`에 `Injector`를 생성해야 합니다. 생성 방법은 MainActivity 에서 설정하였던 방식과 동일합니다.

Fragment 에서  `AndroidInjector` 을 사용하여 `inject`을 수행하려면 Fragement 을 사용할 Activity에 `Injector`를 생성해야 합니다.  Activity에 `HasSupportFragmentInjector` 을 `imprement` 하고 `DispatchingAndroidInjector` 을 바인딩(Binding) 받아 리턴합니다. 

<br/>

**MainActivity.class**

```java
public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector, MainActivityContract.View {

    @Inject MainActivityContract.Presenter presenter;
    @Inject DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }
    //...
}
```

<br/>

`Activity Module`에 `Subcomponent`로 등록하고 AndroidInjector.Factory 에 바인딩 해줄 Component.Builder을 설정합니다. Activity에서도 언급했듯이 AndroidInjector.Factory 은 MultiBinding 을 하기에 FragmentKey로 사용하여 Key를 설정합니다.

<br/>

**MainModule.class**

```java
@Module(subcomponents = MainFragmentComponent.class)
abstract class MainModule {
    @ActivityScope
    @Binds
    abstract MainActivityContract.View bindView(MainActivity activity);

    @ActivityScope
    @Binds
    public abstract MainActivityContract.Presenter bindPresenter(MainPresenterImpl mainPresenter);

    @Binds
    @IntoMap
    @FragmentKey(MainFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindMoviesFragment(MainFragmentComponent.Builder builder);
}
```

> **@FragmentKey 입력 시 주의사항**
>
> `FragmentKey` 는 `dagger.android.support` 와 `dagger.android` 두 패키지에 모두 있습니다. 따라서 FragmentKey로 사용하려는 Fragment가 `android.support.v4.app.Fragment`와 `android.app.Fragment` 패키지 중 소속되어 있는지 확인 후 알맞은 패키지를 `import` 해야 합니다. 

<br/>

Fragment Component 에서 AndroidSupportInjector을 사용하였기에 Fragment 에서는 inject 을 호출만으로 바인딩 할 수 있습니다.

```java
public class MainFragment extends Fragment implements MainFragmentContract.View{

    @Inject MainFragmentContract.Presenter presenter;
  
    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }
}
```

<br/>

# 정리

[DI](https://github.com/FaithDeveloper/TIL/blob/master/Android/DI%20%EB%9E%80%20%EB%AC%B4%EC%97%87%EC%9D%B8%EA%B0%80%3F.md) 부터 시작해서 [Dagger](https://github.com/FaithDeveloper/TIL/blob/master/Android/Dagger%20%EA%B0%84%EB%8B%A8%ED%9E%88%20%EC%95%8C%EC%95%84%EB%B3%B4%EA%B8%B0.md) 와 Android Dagger에 대한 내용을 포스트 하였습니다.  의존성을 요청받으면 `Subcomponent`, `Component`, `Inject 생성자` 순으로 검색하여 주입하는 것을 확인 할 수 있습니다. 

Dagger의 주요 기능은 Android Dager 에서 `AndroidInjector` 을 제공하여 간단하게 Inject 할 수 있는 것과 Scope 을 통하여 `@AndroidScope`, `@FragmentScope` 을 통하여 Lifecycle 에 맞춰서 설정할 수 있는 것을 알 수 있습니다. 

 Dagger 의 진입 장벽이 높지만 DI 을 통하여 개발한다면 유닛테스트 전환 및 유지보수에 도움이 될 것으로 판단됩니다. 하지만 진입 장벽이 높은 만큼 활용 범위는 고민해야 할 것으로 생각됩니다.

사용한 예제에 대한 [소스코드](https://github.com/FaithDeveloper/SampleDaggerForAndroid)를 참고하여 직접 작성해보시면 Android Dagger를 이해하는데 도움이 될 것으로 예상됩니다.



끝으로 Dagger을 이해하는데 잘 정리한 포스트를 제공해준 개발자 분들에게 감사의 뜻을 전합니다.

<br/>

# 참고

[Multi Module 과 Dagger2](https://medium.com/@jsuch2362/multi-module-%EA%B3%BC-dagger2-8472492eaba3)

[DI, Dagger2 란?](http://duzi077.tistory.com/168)

[DI(Dependency Injection) 와 Dagger2](https://cmcmcmcm.blog/2017/08/02/didependency-injection-%EC%99%80-dagger2-2/)

[DI 기본개념부터 사용법까지, Dagger2 시작하기](https://cmcmcmcm.blog/2017/08/15/dagger2-를-이용한-안드로이드에서의-di-구현)

[Dagger2 학습에 필요한 참고자료](https://www.androidhuman.com/life/2016/06/06/dagger2_resources/)

[Dagger Open Source](https://github.com/google/dagger)

