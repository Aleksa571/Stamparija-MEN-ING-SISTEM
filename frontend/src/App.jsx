import { Route, Routes, useLocation } from 'react-router-dom';
import { AnimatePresence } from 'framer-motion';
import Navbar from './components/layout/Navbar.jsx';
import Footer from './components/layout/Footer.jsx';
import ScrollToTop from './components/common/ScrollToTop.jsx';
import ScrollToTopButton from './components/common/ScrollToTopButton.jsx';
import PageTransition from './components/common/PageTransition.jsx';
import HomePage from './pages/HomePage.jsx';
import CategoriesPage from './pages/CategoriesPage.jsx';
import ProductsPage from './pages/ProductsPage.jsx';
import ProductDetailPage from './pages/ProductDetailPage.jsx';
import BlogPage from './pages/BlogPage.jsx';
import BlogDetailPage from './pages/BlogDetailPage.jsx';
import AboutPage from './pages/AboutPage.jsx';
import ContactPage from './pages/ContactPage.jsx';
import LoginPage from './pages/LoginPage.jsx';
import RegisterPage from './pages/RegisterPage.jsx';
import MyOrdersPage from './pages/MyOrdersPage.jsx';
import OrderPage from './pages/OrderPage.jsx';
import NotFoundPage from './pages/NotFoundPage.jsx';
import ProtectedRoute from './auth/ProtectedRoute.jsx';
import AdminLayout from './pages/admin/AdminLayout.jsx';
import AdminDashboard from './pages/admin/AdminDashboard.jsx';
import AdminProducts from './pages/admin/AdminProducts.jsx';
import AdminCategories from './pages/admin/AdminCategories.jsx';
import AdminOrders from './pages/admin/AdminOrders.jsx';
import AdminBlog from './pages/admin/AdminBlog.jsx';
import AdminUsers from './pages/admin/AdminUsers.jsx';

const withTransition = (element) => <PageTransition>{element}</PageTransition>;

function App() {
  const location = useLocation();

  return (
    <div className="d-flex flex-column min-vh-100">
      <Navbar />
      <ScrollToTop />
      <main className="flex-grow-1">
        <AnimatePresence mode="wait">
          <Routes location={location} key={location.pathname}>
            <Route path="/" element={withTransition(<HomePage />)} />
            <Route path="/kategorije" element={withTransition(<CategoriesPage />)} />
            <Route path="/proizvodi" element={withTransition(<ProductsPage />)} />
            <Route path="/proizvodi/:id" element={withTransition(<ProductDetailPage />)} />
            <Route path="/blog" element={withTransition(<BlogPage />)} />
            <Route path="/blog/:id" element={withTransition(<BlogDetailPage />)} />
            <Route path="/o-nama" element={withTransition(<AboutPage />)} />
            <Route path="/kontakt" element={withTransition(<ContactPage />)} />
            <Route path="/prijava" element={withTransition(<LoginPage />)} />
            <Route path="/registracija" element={withTransition(<RegisterPage />)} />
            <Route
              path="/moje-porudzbine"
              element={
                <ProtectedRoute>
                  {withTransition(<MyOrdersPage />)}
                </ProtectedRoute>
              }
            />
            <Route
              path="/poruci/:productId"
              element={
                <ProtectedRoute>
                  {withTransition(<OrderPage />)}
                </ProtectedRoute>
              }
            />
            <Route
              path="/admin"
              element={
                <ProtectedRoute requireAdmin>
                  <AdminLayout />
                </ProtectedRoute>
              }
            >
              <Route index element={<AdminDashboard />} />
              <Route path="proizvodi" element={<AdminProducts />} />
              <Route path="kategorije" element={<AdminCategories />} />
              <Route path="porudzbine" element={<AdminOrders />} />
              <Route path="blog" element={<AdminBlog />} />
              <Route path="korisnici" element={<AdminUsers />} />
            </Route>
            <Route path="*" element={withTransition(<NotFoundPage />)} />
          </Routes>
        </AnimatePresence>
      </main>
      <Footer />
      <ScrollToTopButton />
    </div>
  );
}

export default App;
