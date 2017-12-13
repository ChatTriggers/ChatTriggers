if (process.env.TRAVIS_REPO_SLUG === "ChatTriggers/ct.js"
    && process.env.TRAVIS_PULL_REQUEST == "false"
    && (process.env.TRAVIS_BRANCH === "master" || process.env.TRAVIS_BRANCH === "travis")) {

    console.log("Publishing jars...");

    var { exec } = require('child_process');

    exec("mkdir .utility/jars");
    exec("cp -R build/libs/* .utility/jars");

    var FtpClient = require('ftp-client');
    var client = new FtpClient({
        host: 'chattriggers.com',
        port: 21,
        user: process.env.FTP_USER,
        password: process.env.FTP_PASSWORD
    }, {
        logging: 'basic'
    });

    client.connect(function () {
        client.upload(['.utility/jars/*'], '/public_html/versions', {
            overwrite: 'older'
        }, function (result) {
            console.log(result);
            console.log("Published jars to production.");
        });
    });
}