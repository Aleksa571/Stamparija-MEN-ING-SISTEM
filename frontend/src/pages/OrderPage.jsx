import { useEffect, useState } from 'react';
import { Alert, Button, Card, Col, Container, Form, Row, Spinner } from 'react-bootstrap';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { orderApi, productApi } from '../api/services.js';
import { resolveImage } from '../api/client.js';
import { formatPrice, extractApiError } from '../utils/format.js';
import { useAuth } from '../context/AuthContext.jsx';
import { useToast } from '../context/ToastContext.jsx';

const OrderPage = () => {
  const { productId } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const toast = useToast();

  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');
  const [quantity, setQuantity] = useState(1);
  const [form, setForm] = useState({
    deliveryType: 'LICNO_PREUZIMANJE',
    deliveryAddress: user?.address || '',
    contactPhone: '',
    note: '',
  });

  useEffect(() => {
    productApi.get(productId)
      .then((r) => setProduct(r.data))
      .catch(() => setError('Proizvod nije pronađen'))
      .finally(() => setLoading(false));
  }, [productId]);

  const submit = async (e) => {
    e.preventDefault();
    setError('');
    if (form.deliveryType !== 'LICNO_PREUZIMANJE' && !form.deliveryAddress.trim()) {
      setError('Za izabrani način dostave adresa je obavezna.');
      return;
    }
    setSubmitting(true);
    try {
      const payload = {
        deliveryType: form.deliveryType,
        deliveryAddress: form.deliveryType === 'LICNO_PREUZIMANJE' ? null : form.deliveryAddress,
        contactPhone: form.contactPhone,
        note: form.note,
        items: [{ productId: Number(productId), quantity: Number(quantity) }],
      };
      await orderApi.create(payload);
      toast.success('Porudžbina je uspešno kreirana!');
      navigate('/moje-porudzbine');
    } catch (err) {
      setError(extractApiError(err));
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <div className="text-center py-5"><Spinner /></div>;
  if (error && !product) return <Container className="py-5"><Alert variant="danger">{error}</Alert></Container>;

  const total = (Number(product.price) * Number(quantity || 0));

  return (
    <Container className="py-5">
      <Link to={`/proizvodi/${product.id}`} className="text-brand small">
        <i className="bi bi-arrow-left me-1"></i>Nazad na proizvod
      </Link>
      <h2 className="mt-3 mb-4">Poručivanje proizvoda</h2>
      <Row className="g-4">
        <Col md={7}>
          <Card className="border-0 shadow-sm">
            <Card.Body className="p-4">
              <h5 className="mb-3">Podaci o porudžbini</h5>
              {error && <Alert variant="danger">{error}</Alert>}
              <Form onSubmit={submit}>
                <Form.Group className="mb-3">
                  <Form.Label>Količina *</Form.Label>
                  <Form.Control
                    type="number"
                    min="1"
                    max={product.stock}
                    value={quantity}
                    onChange={(e) => setQuantity(e.target.value)}
                    required
                  />
                  <Form.Text className="text-muted">
                    Na stanju: {product.stock} kom
                  </Form.Text>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Način dostave *</Form.Label>
                  <Form.Select
                    value={form.deliveryType}
                    onChange={(e) => setForm({ ...form, deliveryType: e.target.value })}
                  >
                    <option value="LICNO_PREUZIMANJE">Lično preuzimanje (u Smederevu)</option>
                    <option value="DOSTAVA_NA_ADRESU">Dostava na adresu</option>
                    <option value="KURIRSKA_SLUZBA">Kurirska služba</option>
                  </Form.Select>
                </Form.Group>

                {form.deliveryType !== 'LICNO_PREUZIMANJE' && (
                  <Form.Group className="mb-3">
                    <Form.Label>Adresa za dostavu *</Form.Label>
                    <Form.Control
                      value={form.deliveryAddress}
                      onChange={(e) => setForm({ ...form, deliveryAddress: e.target.value })}
                      placeholder="Ulica i broj, Grad, Poštanski broj"
                      required
                    />
                  </Form.Group>
                )}

                <Form.Group className="mb-3">
                  <Form.Label>Kontakt telefon</Form.Label>
                  <Form.Control
                    value={form.contactPhone}
                    onChange={(e) => setForm({ ...form, contactPhone: e.target.value })}
                    placeholder="064/123-456"
                  />
                </Form.Group>

                <Form.Group className="mb-4">
                  <Form.Label>Napomena (opciono)</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={3}
                    value={form.note}
                    onChange={(e) => setForm({ ...form, note: e.target.value })}
                    placeholder="Posebne instrukcije, dezeni, datum isporuke..."
                  />
                </Form.Group>

                <Button type="submit" className="btn-brand w-100 btn-lg" disabled={submitting}>
                  {submitting ? <Spinner size="sm" /> : (
                    <><i className="bi bi-check-circle me-2"></i>Potvrdi porudžbinu</>
                  )}
                </Button>
              </Form>
            </Card.Body>
          </Card>
        </Col>
        <Col md={5}>
          <Card className="border-0 shadow-sm sticky-top" style={{ top: 80 }}>
            <Card.Body className="p-4">
              <h6 className="mb-3">Pregled porudžbine</h6>
              <div className="d-flex gap-3 mb-3">
                <img
                  src={resolveImage(product.imageUrl)}
                  alt={product.name}
                  style={{ width: 80, height: 80, objectFit: 'cover', borderRadius: 4 }}
                />
                <div>
                  <small className="text-muted">{product.categoryName}</small>
                  <div className="fw-semibold">{product.name}</div>
                  {product.dimensions && <small className="text-muted d-block">{product.dimensions}</small>}
                </div>
              </div>
              <hr />
              <div className="d-flex justify-content-between mb-2">
                <span>Cena po komadu:</span>
                <strong>{formatPrice(product.price)}</strong>
              </div>
              <div className="d-flex justify-content-between mb-2">
                <span>Količina:</span>
                <strong>{quantity || 0}</strong>
              </div>
              <hr />
              <div className="d-flex justify-content-between fs-5">
                <span className="fw-bold">UKUPNO:</span>
                <span className="text-brand fw-bold">{formatPrice(total)}</span>
              </div>
              <Alert variant="info" className="small mt-3 mb-0">
                <i className="bi bi-info-circle me-2"></i>
                Nakon kreiranja porudžbine, mi ćemo vas kontaktirati radi potvrde i daljih instrukcija.
              </Alert>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default OrderPage;
