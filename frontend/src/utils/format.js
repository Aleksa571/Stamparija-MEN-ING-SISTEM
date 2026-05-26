export const formatPrice = (price) => {
  return new Intl.NumberFormat('sr-RS', {
    style: 'currency',
    currency: 'RSD',
    minimumFractionDigits: 2,
  }).format(Number(price || 0));
};

export const formatDate = (dateString) => {
  if (!dateString) return '';
  const d = new Date(dateString);
  return d.toLocaleDateString('sr-RS', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
};

export const formatDateOnly = (dateString) => {
  if (!dateString) return '';
  const d = new Date(dateString);
  return d.toLocaleDateString('sr-RS', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  });
};

export const orderStatusLabel = (status) => {
  const map = {
    PRIMLJENA: 'Primljena',
    U_IZRADI: 'U izradi',
    SPREMNA: 'Spremna',
    ISPORUCENA: 'Isporučena',
    OTKAZANA: 'Otkazana',
  };
  return map[status] || status;
};

export const orderStatusBadge = (status) => {
  const map = {
    PRIMLJENA: 'primary',
    U_IZRADI: 'warning',
    SPREMNA: 'info',
    ISPORUCENA: 'success',
    OTKAZANA: 'danger',
  };
  return map[status] || 'secondary';
};

export const deliveryTypeLabel = (type) => {
  const map = {
    LICNO_PREUZIMANJE: 'Lično preuzimanje',
    DOSTAVA_NA_ADRESU: 'Dostava na adresu',
    KURIRSKA_SLUZBA: 'Kurirska služba',
  };
  return map[type] || type;
};

export const extractApiError = (error) => {
  if (error?.response?.data?.message) return error.response.data.message;
  if (error?.response?.data?.validationErrors) {
    return Object.values(error.response.data.validationErrors).join(', ');
  }
  if (error?.message) return error.message;
  return 'Došlo je do nepoznate greške.';
};
