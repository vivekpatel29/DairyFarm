<?php

require './class/db-class.php';

?>

<aside class="main-sidebar sidebar-dark-primary elevation-4">
    <!-- Brand Logo -->
    <a href="home.php" class="brand-link">
      <img src="image/logo.jpg"
           alt="AdminLTE Logo"
           class="brand-image img-circle elevation-3"
           style="opacity: .8">
      <span class="brand-text font-weight-light">DairyFarm</span>
    </a>

    <!-- Sidebar -->
    <div class="sidebar">
      <!-- Sidebar user (optional) -->
      <div class="user-panel mt-3 pb-3 mb-3 d-flex">
        <div class="image">
          <?php
                  $profile_img= mysqli_query($connection, "select * from tb_admin") or die(mysqli_error($connection)); 
                  while($row1 = mysqli_fetch_array($profile_img)){
                           echo "<html>
                               <img class='img-circle' src='{$row1['admin_image']}'
                               </html>";
                  }
            
               ?>
        </div>
        <div class="info">
             <?php
                 $profile= mysqli_query($connection, "select * from tb_admin") or die(mysqli_error($connection)); 
                 while($row = mysqli_fetch_array($profile)){
                     echo "<a href='profile.php' class='d-block'>{$row['admin_name']}";
                 }
               ?>
          
        </div>
      </div>

      <!-- Sidebar Menu -->
      <nav class="mt-2">
        <ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false">
          <!-- Add icons to the links using the .nav-icon class
               with font-awesome or any other icon font library -->

        
           <li class="nav-item has-treeview menu-open">
            <a href="#" class="nav-link active">
              <i class="nav-icon fas fa-user"></i>
              <p>
                User
                <i class="fas fa-angle-left right"></i>
              </p>
            </a>
            <ul class="nav nav-treeview">
              <li class="nav-item">
                <a href="add-user.php" class="nav-link active">
                  <i class="far fa-circle nav-icon"></i>
                  <p>Add User</p>
                </a>
              </li>
              <li class="nav-item">
                <a href="user-table.php" class="nav-link">
                  <i class="far fa-circle nav-icon"></i>
                  <p>View Users</p>
                </a>
              </li>
            </ul>
          </li>
          <li class="nav-item has-treeview">
            <a href="#" class="nav-link">
              <i class="nav-icon fas fa-truck"></i>
              <p>
                Delivery Boy
                <i class="fas fa-angle-left right"></i>
              </p>
            </a>
            <ul class="nav nav-treeview">
              <li class="nav-item">
                <a href="add-deliveryboy.php" class="nav-link">
                  <i class="far fa-circle nav-icon"></i>
                  <p>Add Delivery Boy</p>
                </a>
              </li>
              <li class="nav-item">
                <a href="deliveryboy-table.php" class="nav-link">
                  <i class="far fa-circle nav-icon"></i>
                  <p>View DeliveryBoy </p>
                </a>
              </li>  
            </ul>
          </li>
            <li class="nav-item has-treeview menu-open">
            <a href="#" class="nav-link active">
              <i class="nav-icon fas fa-dolly"></i>
              <p>
                Product
                <i class="fas fa-angle-left right"></i>
              </p>
            </a>
            <ul class="nav nav-treeview">
              <li class="nav-item">
                <a href="add-product.php" class="nav-link active">
                  <i class="far fa-circle nav-icon"></i>
                  <p>Add Product</p>
                </a>
              </li>
              <li class="nav-item">
                <a href="product-table.php" class="nav-link">
                  <i class="far fa-circle nav-icon"></i>
                  <p>View Product list</p>
                </a>
              </li>
            </ul>
          </li>
          <li class="nav-item">
            <a href="ordermaster-table.php" class="nav-link">
              <i class="nav-icon fas fa-receipt"></i>
              <p>
                Order Master
              </p>
            </a>
          </li>
          <li class="nav-item ">
            <a href="performaorder-table.php" class="nav-link active">
              <i class="nav-icon fas fa-cart-plus"></i>
              <p>
                Performa Order
              </p>
            </a>
          </li>
          <li class="nav-item">
            <a href="payment-table.php" class="nav-link">
              <i class="nav-icon fab fa-cc-amazon-pay"></i>
              <p>
                Payment
              </p>
            </a>
          </li>
          <li class="nav-header">Settings</li>
          
          <li class="nav-item">
            <a href="login.php" class="nav-link">
              <i class="nav-icon fas fa-sign-out-alt"></i>
              <p>
                LogOut
              </p>
            </a>
          </li>
      </nav>
      <!-- /.sidebar-menu -->
    </div>
    <!-- /.sidebar -->
  </aside>