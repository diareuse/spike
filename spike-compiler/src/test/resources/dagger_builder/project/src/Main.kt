@dagger.Component
interface AppComponent {
    @dagger.Component.Builder
    interface Builder {
        fun build(): AppComponent
    }
}
