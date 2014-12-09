#!/data1/php/bin/php
<?php
define (IP, '127.0.0.1');
//define (IP, '10.73.12.132');
define (PORT, 11233);
define (QUEUE, 'da_eva_out');

$mcq = new Memcache();
if ($mcq->connect(IP, PORT) === false) {
	print "connect {IP}:{PORT} failed\n";
	exit(1);
}

$buf = '';

$num=3;
while ($num>0) {
	if ( ($buf=$mcq->get(QUEUE)) == false) {
		$num=$num-1;
		sleep(1);
		continue;
	}
	print $buf;
	$num=3;
}
?>
