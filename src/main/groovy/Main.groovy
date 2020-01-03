
class Main {


    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String ACCESS_KEY_ID = "9b1bf64f8f139523076af120cb60c8a9";
    private static final String SECRET_ACCESS_KEY = "db9f26a3da9750d08e3308c4ad7521f4efccba54f06c518cd14bc76b96666903d3b021c5b37b8766a8bd4e017eae3f1862d3ce9f12eaa7caac0aa968c2480c25";


    static void main(final String[] args) {
        def apiWrapper = new VeracodeApiWrapper(ACCESS_KEY_ID, SECRET_ACCESS_KEY)

//        apiWrapper.findApplicationId('GMR Backend')
        apiWrapper.findBuildsRunningOnApplication(595670)
    }
}