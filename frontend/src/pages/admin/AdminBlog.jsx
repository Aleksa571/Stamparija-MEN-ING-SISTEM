import { useEffect, useState } from 'react';
import { Button, Form, Modal, Spinner, Table } from 'react-bootstrap';
import { blogApi } from '../../api/services.js';
import { useToast } from '../../context/ToastContext.jsx';
import { extractApiError, formatDate } from '../../utils/format.js';

const EMPTY = { title: '', content: '', imageUrl: '' };

const AdminBlog = () => {
  const toast = useToast();
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [show, setShow] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState(EMPTY);
  const [saving, setSaving] = useState(false);

  const load = () => {
    setLoading(true);
    blogApi.list().then((r) => setItems(r.data)).finally(() => setLoading(false));
  };
  useEffect(() => { load(); }, []);

  const openAdd = () => { setEditing(null); setForm(EMPTY); setShow(true); };
  const openEdit = (p) => {
    setEditing(p);
    setForm({ title: p.title, content: p.content, imageUrl: p.imageUrl || '' });
    setShow(true);
  };

  const save = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      if (editing) {
        await blogApi.update(editing.id, form);
        toast.success('Objava je ažurirana');
      } else {
        await blogApi.create(form);
        toast.success('Objava je dodata');
      }
      setShow(false);
      load();
    } catch (err) {
      toast.error(extractApiError(err));
    } finally {
      setSaving(false);
    }
  };

  const del = async (p) => {
    if (!window.confirm(`Obrisati objavu "${p.title}"?`)) return;
    try {
      await blogApi.delete(p.id);
      toast.success('Objava obrisana');
      load();
    } catch (err) {
      toast.error(extractApiError(err));
    }
  };

  return (
    <>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h3 className="m-0">Blog</h3>
        <Button className="btn-brand" onClick={openAdd}>
          <i className="bi bi-plus-circle me-1"></i> Nova objava
        </Button>
      </div>
      {loading ? <Spinner /> : (
        <Table responsive hover className="bg-white shadow-sm align-middle">
          <thead className="table-light">
            <tr>
              <th>Naslov</th>
              <th>Autor</th>
              <th>Datum</th>
              <th className="text-end">Akcije</th>
            </tr>
          </thead>
          <tbody>
            {items.map((p) => (
              <tr key={p.id}>
                <td className="fw-semibold">{p.title}</td>
                <td>{p.authorFullName || p.authorUsername}</td>
                <td className="small">{formatDate(p.createdAt)}</td>
                <td className="text-end">
                  <Button size="sm" variant="outline-primary" className="me-2" onClick={() => openEdit(p)}>
                    <i className="bi bi-pencil"></i>
                  </Button>
                  <Button size="sm" variant="outline-danger" onClick={() => del(p)}>
                    <i className="bi bi-trash"></i>
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}

      <Modal show={show} onHide={() => setShow(false)} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>{editing ? 'Izmena' : 'Nova'} objava</Modal.Title>
        </Modal.Header>
        <Form onSubmit={save}>
          <Modal.Body>
            <Form.Group className="mb-3">
              <Form.Label>Naslov *</Form.Label>
              <Form.Control value={form.title}
                            onChange={(e) => setForm({ ...form, title: e.target.value })}
                            required maxLength={200} />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Sadržaj *</Form.Label>
              <Form.Control as="textarea" rows={10} value={form.content}
                            onChange={(e) => setForm({ ...form, content: e.target.value })}
                            required maxLength={10000} />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>URL slike</Form.Label>
              <Form.Control value={form.imageUrl}
                            onChange={(e) => setForm({ ...form, imageUrl: e.target.value })}
                            placeholder="/uploads/IMG_1062.JPG" />
            </Form.Group>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShow(false)}>Otkaži</Button>
            <Button type="submit" className="btn-brand" disabled={saving}>
              {saving ? <Spinner size="sm" /> : 'Sačuvaj'}
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </>
  );
};

export default AdminBlog;
