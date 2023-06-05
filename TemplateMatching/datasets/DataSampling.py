import json
import urllib.request
import string
import random
import DataSampling

def extractVideos():
    count = 50
    API_KEY = 'API KEY'
    rand = ''.join(random.choice(string.ascii_uppercase + string.digits) for _ in range(3))

    urlData = "https://www.googleapis.com/youtube/v3/search?key={}&maxResults={}&part=snippet&type=video&q={}".format(API_KEY,count,rand)
    webURL = urllib.request.urlopen(urlData)
    data = webURL.read()
    encoding = webURL.info().get_content_charset('utf-8')
    results = json.loads(data.decode(encoding))

    videoIds = []
    for data in results['items']:
        videoId = (data['id']['videoId'])
        videoIds.append(videoId)
    return videoIds

def randomVideos():
	urls = []
	videoIds = extractVideos()
	for i in range(len(videoIds)):
		urls.append('https://www.youtube.com/embed/' + str(videoIds[i]))
	return urls

for i in range(200):
	urls = randomVideos()
	for j, url in enumeratet(urls):
		CaptureThumbnail(i*len(urls) + j, url)

