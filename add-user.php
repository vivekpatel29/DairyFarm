<?php
  session_start();
  require './class/db-class.php';

  
  if($_POST){
      $user_name = $_POST['user_name'];
      $user_email= $_POST['user_email'];
      $user_gender= $_POST['user_gender'];
      $user_mobile= $_POST['user_mobile'];
      $user_password= $_POST['user_password'];
      $user_houseno= $_POST['user_houseno'];
      $user_address= $_POST['user_address'];
      $location_id= $_POST['location_id'];
      
      $q  = mysqli_query($connection, "insert into  tb_user(user_name,user_email,user_gender,user_mobile,user_password,user_houseno,user_address,location_id) values('{$user_name}','{$user_email}','{$user_gender}','{$user_mobile}','{$user_password}','{$user_houseno}','{$user_address}','{$location_id}')") or die(mysqli_error($connection));
    
      
      if($q){
          echo '<html><div class="card bg-gradient-success">
              <div class="card-header">
                <h3 class="card-title">Add Successfully</h3>

                <div class="card-tools">
                  <button type="button" class="btn btn-tool" data-card-widget="remove"><i class="fas fa-times"></i>
                  </button>
                </div>
                <!-- /.card-tools -->
              </div></html>';
      } 
  }


?>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>DairyFarm | Add User</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta name="viewport" content="width=device-width, initial-scale=1">
<link class="img-circle" rel="icon" href="./image/logo.jpg"/>
  <!-- Font Awesome -->
  <link rel="stylesheet" href="plugins/fontawesome-free/css/all.min.css">
  <!-- Ionicons -->
  <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="dist/css/adminlte.min.css">
  <!-- Google Font: Source Sans Pro -->
  <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700" rel="stylesheet">
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
  <?php 
   include './themepart/top-menu.php';
   include './themepart/sidebar.php';
  ?>

  <!-- Content Wrapper. Contains page content -->
  
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>User Form</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="#">Home</a></li>
              <li class="breadcrumb-item active">Add User</li>
            </ol>
          </div>
        </div>
      </div><!-- /.container-fluid -->
    </section>

    <!-- Main content -->
    <section class="content">
      <div class="container-fluid">
        <div class="row">
            
          <!-- left column -->
          <div class="col-md-6">
            <!-- general form elements -->
            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title">Add User</h3>
              </div>
              <!-- /.card-header -->
              
              <!-- form start -->
              <form role="form" method="post">
                <div class="card-body">
                  <div class="form-group">
                    <label for="exampleInputEmail1">User Name</label>
                    <input type="text" name="user_name" class="form-control" id="exampleInputEmail1" placeholder="Enter Username" required>
                  </div>
                      <div class="form-group">
                    <label for="exampleInputEmail1">User Email</label>
                    <input type="email" name="user_email" class="form-control" id="exampleInputEmail1" placeholder="Enter Email">
                  </div>
                     <div class="form-group">
                    <label for="exampleInputEmail1">Gender</label>
                    <input type="text" name="user_gender" class="form-control" id="exampleInputEmail1" placeholder="Enter Gender" required>
                  </div>
                     <div class="form-group">
                    <label for="exampleInputEmail1">User Mobile Number</label>
                    <input type="number"  name="user_mobile" class="form-control" id="exampleInputEmail1" placeholder="Enter User Mobile Number" required>
                  </div>
                  <div class="form-group">
                    <label for="exampleInputPassword1">Password</label>
                    <input type="password" name="user_password" class="form-control" id="exampleInputPassword1" placeholder="Password" required>
                  </div>
                     <div class="form-group">
                    <label for="exampleInputEmail1">House Number</label>
                    <input type="number" name="user_houseno" class="form-control" id="exampleInputEmail1" placeholder="Enter House No. " required>
                  </div>
                     <div class="form-group">
                    <label for="exampleInputEmail1">Address</label>
                    <input type="text" name="user_address" class="form-control" id="exampleInputEmail1" placeholder="Enter Address" required>
                  </div>

                  <div class="form-group">
                    <label for="exampleInputEmail1">LOcation Id</label>
                    <input type="number" name="location_id" class="form-control" id="exampleInputEmail1" placeholder="Enter LOcation ID " required>
                  </div>

                <!-- /.card-body -->

                <div class="card-footer">
                  <button type="submit" class="btn btn-primary">Submit</button>
                </div>
              </form>
            </div>
           

          </div>
         
        </div>
        <!-- /.row -->
      </div><!-- /.container-fluid -->
    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->
  <footer class="main-footer">
    <?php 
      include './themepart/footer.php';
    ?>
  </footer>

  <!-- Control Sidebar -->
  <aside class="control-sidebar control-sidebar-dark">
    <!-- Control sidebar content goes here -->
  </aside>
  <!-- /.control-sidebar -->
</div>
<!-- ./wrapper -->

<!-- jQuery -->
<script src="plugins/jquery/jquery.min.js"></script>
<!-- Bootstrap 4 -->
<script src="plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<!-- bs-custom-file-input -->
<script src="plugins/bs-custom-file-input/bs-custom-file-input.min.js"></script>
<!-- AdminLTE App -->
<script src="dist/js/adminlte.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="dist/js/demo.js"></script>
<script type="text/javascript">
$(document).ready(function () {
  bsCustomFileInput.init();
});
</script>
</body>
</html>
