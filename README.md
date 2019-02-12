# rstp-web-player

  the class `Converter` is based on javacv that can do the same thing as ffmpeg.It pull the stream from a rtsp uri and push the stream to the http 
  server. the class `UdpServer` is based on netty.It receives the stream from `Converter` and wraps stream as binary websocket frame. the class `WebSocketServer` pushes data to play.html
  
## start steps
1. run `NettyServer`
2. run `Converter`
3. open `play.html`
