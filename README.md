Sample Flickr App

Feature:
- On start of app, fetch the images from public feed https://api.flickr.com/services/feeds/photos_public.gne?tags=cat&format=json&nojsoncallback=1

- Receive data in json format (it returns pjson format, by adding nojsoncallback=1 as parameter, it would return json response

- Parse the response

- Show image title and image in gridview (Columns - dynamic according to screen width)

- On click of gridview item, show a modal dialog with cross button at top displaying authorname and image of selected item

Additional Feature:
- Zoom the modal image 

Components used:

- static data binding- butterknife

- For Networking - Retrofit 2

- For parsing - gson, fasterxml json(for jsonignoreproperties mainly)

- For image loading - Glide

Architecture

- Used MVP

Limitation

- Since no of images available for above api is always 20, loading indicator showing loading of image is for very small duration

- If in api, page number is mentioned, next set of images can be properly fetched and added to recyclerview using notifydatasetchanged. (Loading indicator would be for more time). Hence proper and advantageous pagination


