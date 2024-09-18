Hidden Gems android application (on Iguana Android Studio) is an app where tourists can post less known tourist attractions (hidden gems).

Each user can post hidden gems, rate them, comment on them, add them to their favorites or mark them as visited. They can edit their profiles and see the gems they've created and visited on their profiles.

The application caches data in room and syncronizes with firestore to get latest updates.

The application stores images in firebase storage, and does user authentication with firebase authentication.

*the app also gets data from external api to give user the top tourist destination of the day

Files:
Dao folder contains config of room and dao

Model:
Model folder contains files that handle data

-converters: convert types to room
-relationships: relationships of tables in db
-views: views of db. CommentWithUser view is used to get data of comments and the data of the users who left them for querying. 
-room table classes: Category, City, Comment, Gem, Ratings, User,
-Model: runs data functions and communicates between room and app and firebase and app
-FirebaseModel: performs firebase operations

Modules:
App files
-Adapters folder contains recycler view adapters 
-other folders hold app fragments: AddEditGem (which can act as add gem or edit gem fragment based on its mode), EditProfile, Favorites, Feed, LogIn, Profile, Search, SignUp, ViewGem
-Request and CountryName: classes hold data structure of external api response
-MainActivity: manages fragments, menu, back, and contains other functions which are used by other fragments of the app

Res folder holds drawable, fragment layouts, saved strings, etc
