import { createContext, useCallback, useContext, useState } from 'react';
import { Toast, ToastContainer } from 'react-bootstrap';

const ToastContext = createContext(null);

export const ToastProvider = ({ children }) => {
  const [toasts, setToasts] = useState([]);

  const show = useCallback((message, variant = 'success', delay = 4000) => {
    const id = Date.now() + Math.random();
    setToasts((prev) => [...prev, { id, message, variant }]);
    setTimeout(() => {
      setToasts((prev) => prev.filter((t) => t.id !== id));
    }, delay);
  }, []);

  const success = (m) => show(m, 'success');
  const error = (m) => show(m, 'danger');
  const info = (m) => show(m, 'info');
  const warn = (m) => show(m, 'warning');

  return (
    <ToastContext.Provider value={{ show, success, error, info, warn }}>
      {children}
      <ToastContainer position="top-end" className="p-3 toast-container-custom">
        {toasts.map((t) => (
          <Toast key={t.id} bg={t.variant} className="text-white">
            <Toast.Body className="d-flex align-items-center">
              <i className={`bi ${t.variant === 'danger' ? 'bi-exclamation-octagon' : t.variant === 'success' ? 'bi-check-circle' : 'bi-info-circle'} me-2 fs-5`}></i>
              {t.message}
            </Toast.Body>
          </Toast>
        ))}
      </ToastContainer>
    </ToastContext.Provider>
  );
};

export const useToast = () => {
  const ctx = useContext(ToastContext);
  if (!ctx) throw new Error('useToast mora biti u ToastProvider');
  return ctx;
};
