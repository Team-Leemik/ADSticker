let cache = {} // API 사용시 응답을 캐싱하는 객체
let cacheTimes = [] // 캐시된 데이터가 언제 요청되었는지를 저장하는 배열
let cacheDuration = 600000 // 캐시 유효기간 : 10분을 기준으로 한다.

chrome.runtime.onInstalled.addListener(() => {
  chrome.storage.sync.get({cacheDuration: 600000}, function(settings) {
    if (settings && settings.cacheDuration !== undefined) {
      cacheDuration = settings.cacheDuration
    }
  })
})

chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  switch (message.query) {
    case 'insertCss':
      chrome.scripting.insertCSS({
        target: {
          tabId: sender.tab.id,
        },
        files: message.files,
      })
      break
    
    case 'videoApiRequest':
      // Remove expired cache data.
      const now = Date.now()
      let numRemoved = 0
      console.log(cacheTimes)
      for (const [fetchTime, videoId] of cacheTimes) {
        console.log('[fetchTime, videoId]',fetchTime,videoId)
        if (now - fetchTime > cacheDuration) {
          delete cache[videoId]
          numRemoved++
        } else {
          break
        }
      }
      
      if (numRemoved > 0) {
        cacheTimes = cacheTimes.slice(numRemoved)
      }

      if (message.videoId in cache) {
        // Use cached data if it exists.
        sendResponse(cache[message.videoId])
        return
      }

      // Otherwise, fetch new data and cache it.
      fetch('http://localhost:8080/url', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({url: `https://www.youtube.com/watch?v=${message.videoId}`}),
      }).then(response => {
          if (!response.ok) {
            sendResponse(null)
          } else {
            response.json().then(data => {
              const isAdIncluded = {
                'flag' : data.flag
              }
              if (!(message.videoId in cache)) {
                cache[message.videoId] = isAdIncluded
                cacheTimes.push([Date.now(), message.videoId])
              }
              console.log(data)
              console.log(data)
              console.log(data)
              sendResponse(isAdIncluded)
            })
          }
        })
      // Returning `true` signals to the browser that we will send our
      // response asynchronously using `sendResponse()`.
      return true
  }
})
