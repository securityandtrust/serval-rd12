var http = require('http');
var fs = require('fs');
var path = require('path');


http.createServer(function (request, response) {




    var filePath = '.' + decodeURIComponent(request.url);
    if (filePath == './')
        filePath = './index.html';

    console.log('request starting. File requested:' + filePath);

    var extname = path.extname(filePath);
    var contentType = 'text/html';
    switch (extname) {
        case '.js':
            contentType = 'text/javascript';
            break;
        case '.css':
            contentType = 'text/css';
            break;
        case '.png':
            contentType = 'image/png';
            break;
	        case '.mov':
	            contentType = 'video/quicktime';
	            break;
		        case '.gif':
		            contentType = 'image/gif';
		            break;
        default:
            console.log('File type not recognized:' + extname);
    }

    path.exists(filePath, function(exists) {

        if (exists) {
            fs.readFile(filePath, function(error, content) {
                if (error) {
                    response.writeHead(500);
                    response.end();
                }
                else {
                    response.writeHead(200, { 'Content-Type': contentType });
                    response.end(content, 'utf-8');
                }
            });
        }
        else {
            response.writeHead(404);
            response.end();
        }
    });

}).listen(8022);

//console.log('Server running at http://127.0.0.1:8125/');