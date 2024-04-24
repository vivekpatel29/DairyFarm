<?php

$host="localhost";
$username= "root";
$passwd= "";
$dbname= "dairyfarm";



$connection= mysqli_connect($host, $username, $passwd, $dbname);


if(!$connection){
    echo "<h1> Error in Database connection</h1>";
    die();
}


