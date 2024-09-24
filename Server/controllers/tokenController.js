
import jwt from 'jsonwebtoken';
const secretKey = 'YoutyoubeProject2024';


function getKey() {
    return secretKey;
}

function decodeToken(req, res) {
    const token = req.session.token

    try {
        getKey();
        // Verify and decode the JWT
        const decoded = jwt.verify(token, getKey());
        // Access the decoded payload
        const userId = decoded.id;
        return userId;
    } catch (error) {
        return res.status(401).json({ message: 'Invalid token' });
    }
}

function authenticateToken(req, res, next) {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[0];
  
    if (token == null) return res.sendStatus(401); // If there's no token, return 401 Unauthorized
  
    jwt.verify(token, getKey(), (err, user) => {
      if (err) return res.sendStatus(403); // If token is invalid, return 403 Forbidden
        
      req.userObjectID = user.id;
      //req.userObjectID = user; // Save the decoded user information in the request object
      next(); // Pass control to the next middleware/handler
    });
  }

export { getKey, decodeToken, authenticateToken };