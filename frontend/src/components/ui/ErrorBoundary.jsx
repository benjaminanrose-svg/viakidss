import { Component } from 'react';
import { AlertTriangle, RefreshCw } from 'lucide-react';

export class ErrorBoundary extends Component {
    constructor(props) {
        super(props);
        this.state = { hasError: false, error: null, errorInfo: null };
    }

    static getDerivedStateFromError(error) {
        return { hasError: true, error };
    }

    componentDidCatch(error, errorInfo) {
        this.setState({ error, errorInfo });
        console.error('ErrorBoundary caught:', error, errorInfo);
    }

    render() {
        if (this.state.hasError) {
            return (
                <div style={{ minHeight: '100vh', background: '#1e293b', display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '24px' }}>
                    <div style={{ background: 'white', borderRadius: '16px', padding: '32px', maxWidth: '500px', width: '100%', textAlign: 'center' }}>
                        <h2 style={{ fontSize: '20px', fontWeight: 'bold', color: '#dc2626', marginBottom: '12px' }}>Error detectado</h2>
                        <p style={{ color: '#374151', fontSize: '14px', marginBottom: '16px', wordBreak: 'break-all' }}>
                            {this.state.error?.message || 'Error desconocido'}
                        </p>
                        <button
                            onClick={() => window.location.reload()}
                            style={{ background: '#2563eb', color: 'white', padding: '10px 24px', borderRadius: '8px', border: 'none', cursor: 'pointer', fontWeight: 'bold' }}
                        >
                            Recargar Página
                        </button>
                    </div>
                </div>
            );
        }
        return this.props.children;
    }
}
