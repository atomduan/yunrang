#!/data1/php/bin/php
<?php
define (IP, '127.0.0.1');
define (PORT, 11233);
define (QUEUE, 'da_eva_in');

$mcq = new Memcache();
if ($mcq->connect(IP, PORT) === false) {
	print "connect {IP}:{PORT} failed\n";
	exit(1);
}

$fp = fopen("/dev/stdin", "r");
$buf = '';
$buf_bad = false;
$nr = -1;

$URL_REGEX="/^@URL:http:\/\/t.sina.com.cn\/[0-9]+\/[0-9a-zA-Z]+$/";

$line = '';
while ($line !== false) {
	$line = fgets($fp);

	if ($line !== "@\n" && $line !== false) {
		if (strpos($line, '@URL:') !== false) {
			if (!preg_match($URL_REGEX, $line)) {
				$buf_bad=true;
			} 
		}
		if (strpos($line, '@CONTENT:') !== false) {
			$line = str_replace('</a>', '', $line);
			$line = preg_replace('/<a.*?>/', '', $line);
			$line = preg_replace('/<.*?\/>/', '', $line);
			if (strpos($line, '//@') !== false) {
				$line = substr($line, 0, strpos($line, '//@'))."\n";
			}
			if (strlen($line) > 200) {
				$line = substr($line, 0, 200)."\n";
			}	
		}
		if (strpos($line, '@TOPIC_WORDS') === false &&
		    strpos($line, '@TOPIC_RELEATED_WORDS') === false &&
		    strpos($line, '@NON_TOPIC_WORDS') === false) {
			$buf .= $line;
		}
		continue;
	}
	$nr ++;
	if (!$buf_bad) {
		while ($buf != '' && $mcq->set(QUEUE, $buf) === false) {
			print "put {QUEUE}: '$buf' failed\n";
			sleep(2);
		}
	}
	$buf = $line;
	$buf .= "@ACTION:A\n";
	$buf_bad = false;
}

echo "put $nr\n";
?>
