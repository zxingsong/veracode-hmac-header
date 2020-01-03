
class Main {


    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String ACCESS_KEY_ID = "";
    private static final String SECRET_ACCESS_KEY = "";


    static void main(final String[] args) {
        def apiWrapper = new VeracodeApiWrapper(ACCESS_KEY_ID, SECRET_ACCESS_KEY)

//        apiWrapper.findApplicationId('GMR Backend')
        apiWrapper.findBuildsRunningOnApplication(595670)
    }
}
