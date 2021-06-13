package com.benni.hiltplayground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.solver.widgets.analyzer.Dependency
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Singleton

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //field injection
    @Inject
    lateinit var someClass: SomeClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        println(someClass.doAThing())    //field injection


//        println(someClass.doSomeOtherThing())    //constructor injection


    }
}

@AndroidEntryPoint
class MyFragment: Fragment(){

    @Inject
    lateinit var someClass: SomeClass

}

//@ActivityScoped
class SomeClass
@Inject
constructor(
//    private val someOtherClass: SomeOtherClass
    private val someInterfaceImpl: SomeInterface,
    private val gson: Gson
) {

    fun doAThing(): String {
        return "Look I got: ${someInterfaceImpl.getAThing()}"
    }
}

class SomeInterfaceImpl
@Inject
constructor(
    private val someDependency: String
) : SomeInterface {
    override fun getAThing(): String {
        return "A Thing, $someDependency"
    }
}

interface SomeInterface{

    fun getAThing(): String
}

@InstallIn(ApplicationComponent::class) //install in the scope scelected, applicationcomponent in this case
                                        //be careful of the singleto call later (check scope page in the doc.
@Module
//more complex and not working always
class MyModule{
    @Singleton
    @Provides
    fun provideSomeString(): String{
        return "some String"
    }
    @Singleton
    @Provides
    fun provideSomeInterface(
        somestring: String
    ): SomeInterface{
        return SomeInterfaceImpl(somestring)
    }

    @Singleton
    @Provides
    fun provideGson(): Gson{
        return Gson()
    }

}