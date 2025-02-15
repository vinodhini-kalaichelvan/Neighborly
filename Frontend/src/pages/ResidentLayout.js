import React from 'react';
import MainLayout from './Mainlayout';
import { Outlet } from 'react-router-dom';

const ResidentLayout = () => {
  return <MainLayout role="resident" >
    <Outlet />
    </MainLayout>;
};
export default ResidentLayout;
