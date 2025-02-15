import React from 'react';
import MainLayout from './Mainlayout';
import { Outlet } from 'react-router-dom';

const AdminLayout = () => {
  return <MainLayout role="admin" >
    <Outlet />
    </MainLayout>;
};
export default AdminLayout;
