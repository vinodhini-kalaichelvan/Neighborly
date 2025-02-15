import React from 'react';
import MainLayout from './Mainlayout';
import { Outlet } from 'react-router-dom';

const ManagerLayout = () => {
  return <MainLayout role="manager" >
    <Outlet />
    </MainLayout>;
};
export default ManagerLayout;
